import { Component, OnInit } from '@angular/core';
import { TransactionsService, FinancialReport } from '../transactions.service';
import { ChartConfiguration } from 'chart.js';
import { CommonModule } from '@angular/common';
import { BaseChartDirective } from 'ng2-charts';
import { ActivatedRoute } from '@angular/router';
import { ApiService } from '../../../core/services/api.service';

// Export libraries
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import * as XLSX from 'xlsx';

// Material
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule } from '@angular/material/icon';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { LoadingSpinnerComponent } from '../../../shared/components/loading-spinner/loading-spinner.component';
import { CurrencyXofPipe } from '../../../shared/pipes/currency-xof.pipe';

interface CeremonialYear {
  id: number;
  year: number;
  active: boolean;
}

@Component({
  selector: 'app-financial-report',
  standalone: true,
  imports: [
    CommonModule,
    BaseChartDirective,
    MatCardModule,
    MatButtonModule,
    MatSelectModule,
    MatProgressSpinnerModule,
    MatIconModule,
    MatGridListModule,
    MatFormFieldModule,
    MatInputModule,
    LoadingSpinnerComponent,
    CurrencyXofPipe
  ],
  templateUrl: './financial-report.component.html',
  styleUrls: ['./financial-report.component.scss']
})
export class FinancialReportComponent implements OnInit {
  report: FinancialReport | null = null;
  loading = true;
  ceremonialYears: CeremonialYear[] = [];
  selectedYearId: number | null = null;

  // Graphique dépenses par catégorie
  public pieChartData: ChartConfiguration<'pie'>['data'] = {
    labels: [],
    datasets: [{
      data: [],
      backgroundColor: [
        '#FF6384',
        '#36A2EB',
        '#FFCE56',
        '#4BC0C0',
        '#9966FF',
        '#FF9F40',
        '#FF6384'
      ]
    }]
  };

  public pieChartOptions: ChartConfiguration<'pie'>['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'bottom'
      }
    }
  };

  constructor(
    private transactionsService: TransactionsService,
    private api: ApiService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.loadCeremonialYears();
  }

  loadCeremonialYears(): void {
    this.api.get('/ceremonial-years').subscribe({
      next: (response: any) => {
        this.ceremonialYears = response.data;
        // Sélectionner l'année active par défaut
        const activeYear = this.ceremonialYears.find(y => y.active);
        if (activeYear) {
          this.selectedYearId = activeYear.id;
          this.loadReport();
        }
      },
      error: () => {
        // Fallback avec une année par défaut
        this.selectedYearId = 1;
        this.loadReport();
      }
    });
  }

  onYearChange(): void {
    this.loadReport();
  }

  loadReport(): void {
    if (!this.selectedYearId) return;

    this.loading = true;
    this.transactionsService.getFinancialReport(this.selectedYearId).subscribe({
      next: (response) => {
        this.report = response.data;
        this.updateChart();
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  updateChart(): void {
    if (!this.report) return;

    const categories = Object.keys(this.report.expensesByCategory);
    const values = Object.values(this.report.expensesByCategory);

    this.pieChartData.labels = categories;
    this.pieChartData.datasets[0].data = values;
  }

  formatCurrency(value: number): string {
    return new Intl.NumberFormat('fr-FR', {
      style: 'currency',
      currency: 'XOF'
    }).format(value);
  }

  exportPDF(): void {
    if (!this.report) return;

    const doc = new jsPDF();

    // Add title
    doc.setFontSize(20);
    doc.text('Rapport Financier', 14, 20);

    // Add year info
    const selectedYear = this.ceremonialYears.find(y => y.id === this.selectedYearId);
    doc.setFontSize(12);
    doc.text(`Année: ${selectedYear?.year || 'N/A'}`, 14, 35);
    doc.text(`Généré le: ${new Date().toLocaleDateString('fr-FR')}`, 14, 45);

    // Financial summary
    doc.setFontSize(14);
    doc.text('Résumé Financier', 14, 60);

    const summaryData = [
      ['Budget Initial', this.formatCurrency(this.report.initialBudget)],
      ['Total Revenus', this.formatCurrency(this.report.totalIncome)],
      ['Total Dépenses', this.formatCurrency(this.report.totalExpense)],
      ['Balance', this.formatCurrency(this.report.balance)]
    ];

    autoTable(doc, {
      startY: 70,
      head: [['Catégorie', 'Montant']],
      body: summaryData,
      styles: { fontSize: 10 },
      headStyles: { fillColor: [41, 128, 185] }
    });

    // Contributions statistics
    let finalY = (doc as any).lastAutoTable.finalY + 20;
    doc.setFontSize(14);
    doc.text('Statistiques des Cotisations', 14, finalY);

    const contributionsData = [
      ['Total Cotisations', this.formatCurrency(this.report.totalContributions)],
      ['Cotisations Payées', this.report.paidContributionCount.toString()],
      ['Cotisations Non Payées', this.report.unpaidContributionCount.toString()],
      ['Nombre de Transactions', this.report.transactionCount.toString()]
    ];

    autoTable(doc, {
      startY: finalY + 10,
      head: [['Statistique', 'Valeur']],
      body: contributionsData,
      styles: { fontSize: 10 },
      headStyles: { fillColor: [46, 125, 50] }
    });

    // Expenses by category
    finalY = (doc as any).lastAutoTable.finalY + 20;
    doc.setFontSize(14);
    doc.text('Dépenses par Catégorie', 14, finalY);

    const categoryData = Object.entries(this.report.expensesByCategory).map(([category, amount]) => [
      category,
      this.formatCurrency(amount)
    ]);

    if (categoryData.length > 0) {
      autoTable(doc, {
        startY: finalY + 10,
        head: [['Catégorie', 'Montant']],
        body: categoryData,
        styles: { fontSize: 10 },
        headStyles: { fillColor: [230, 74, 25] }
      });
    }

    // Recent transactions
    finalY = (doc as any).lastAutoTable.finalY + 20;
    doc.setFontSize(14);
    doc.text('Transactions Récentes', 14, finalY);

    const transactionsData = this.report.recentTransactions.slice(0, 10).map(transaction => [
      transaction.transactionDate ? new Date(transaction.transactionDate).toLocaleDateString('fr-FR') : '',
      transaction.description,
      transaction.category,
      transaction.type === 'SORTIE' ? '-' + this.formatCurrency(transaction.amount) : '+' + this.formatCurrency(transaction.amount)
    ]);

    if (transactionsData.length > 0) {
      autoTable(doc, {
        startY: finalY + 10,
        head: [['Date', 'Description', 'Catégorie', 'Montant']],
        body: transactionsData,
        styles: { fontSize: 8 },
        headStyles: { fillColor: [149, 165, 166] }
      });
    }

    // Save the PDF
    doc.save(`rapport-financier-${selectedYear?.year || 'annee'}.pdf`);
  }

  exportExcel(): void {
    if (!this.report) return;

    const selectedYear = this.ceremonialYears.find(y => y.id === this.selectedYearId);

    // Create workbook
    const workbook = XLSX.utils.book_new();

    // Summary sheet
    const summaryData = [
      { 'Métrique': 'Année', 'Valeur': selectedYear?.year || 'N/A' },
      { 'Métrique': 'Budget Initial', 'Valeur': this.report.initialBudget },
      { 'Métrique': 'Total Revenus', 'Valeur': this.report.totalIncome },
      { 'Métrique': 'Total Dépenses', 'Valeur': this.report.totalExpense },
      { 'Métrique': 'Balance', 'Valeur': this.report.balance },
      { 'Métrique': 'Total Cotisations', 'Valeur': this.report.totalContributions },
      { 'Métrique': 'Cotisations Payées', 'Valeur': this.report.paidContributionCount },
      { 'Métrique': 'Cotisations Non Payées', 'Valeur': this.report.unpaidContributionCount },
      { 'Métrique': 'Nombre de Transactions', 'Valeur': this.report.transactionCount }
    ];

    const summarySheet = XLSX.utils.json_to_sheet(summaryData);
    XLSX.utils.book_append_sheet(workbook, summarySheet, 'Résumé');

    // Expenses by category sheet
    const categoryData = Object.entries(this.report.expensesByCategory).map(([category, amount]) => ({
      'Catégorie': category,
      'Montant': amount
    }));

    if (categoryData.length > 0) {
      const categorySheet = XLSX.utils.json_to_sheet(categoryData);
      XLSX.utils.book_append_sheet(workbook, categorySheet, 'Dépenses par Catégorie');
    }

    // Recent transactions sheet
    const transactionsData = this.report.recentTransactions.map(transaction => ({
      'Date': transaction.transactionDate ? new Date(transaction.transactionDate).toLocaleDateString('fr-FR') : '',
      'Description': transaction.description,
      'Catégorie': transaction.category,
      'Type': transaction.type,
      'Montant': transaction.amount,
      'Méthode de Paiement': transaction.paymentMethod,
      'Numéro de Référence': transaction.referenceNumber,
      'Membre': transaction.memberName || '',
      'Enregistré par': transaction.recordedByName || ''
    }));

    if (transactionsData.length > 0) {
      const transactionsSheet = XLSX.utils.json_to_sheet(transactionsData);
      XLSX.utils.book_append_sheet(workbook, transactionsSheet, 'Transactions Récentes');
    }

    // Save the file
    XLSX.writeFile(workbook, `rapport-financier-${selectedYear?.year || 'annee'}.xlsx`);
  }
}