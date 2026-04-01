import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ContributionsService, Contribution } from '../contributions.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { LoadingSpinnerComponent } from '../../../shared/components/loading-spinner/loading-spinner.component';
import { ConfirmDialogComponent } from '../../../shared/components/confirm-dialog/confirm-dialog.component';
import { CurrencyXofPipe } from '../../../shared/pipes/currency-xof.pipe';

// Export libraries
import jsPDF from 'jspdf';
import 'jspdf-autotable';
import * as XLSX from 'xlsx';

@Component({
  selector: 'app-contributions-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatDialogModule,
    LoadingSpinnerComponent,
    CurrencyXofPipe
  ],
  templateUrl: './contributions-list.component.html',
  styleUrls: ['./contributions-list.component.scss']
})
export class ContributionsListComponent implements OnInit {
  displayedColumns: string[] = ['member', 'amount', 'date', 'status', 'actions'];
  dataSource: MatTableDataSource<Contribution>;
  contributions: Contribution[] = [];
  filteredContributions: Contribution[] = [];
  loading = true;
  searchTerm = '';

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private contributionsService: ContributionsService,
    private router: Router,
    private dialog: MatDialog,
    private toastr: ToastrService
  ) {
    this.dataSource = new MatTableDataSource<Contribution>([]);
  }

  ngOnInit(): void {
    this.loadContributions();
  }

  loadContributions(): void {
    this.loading = true;
    this.contributionsService.getContributions().subscribe({
      next: (data: any) => {
        this.contributions = Array.isArray(data) ? data : (data.data || []);
        this.filteredContributions = this.contributions;
        this.dataSource.data = this.contributions;
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
        this.loading = false;
      },
      error: () => {
        this.toastr.error('Erreur de chargement');
        this.loading = false;
      }
    });
  }

  navigateToAdd(): void {
    this.router.navigate(['/contributions/new']);
  }

  editContribution(id: number): void {
    this.router.navigate([`/contributions/edit/${id}`]);
  }

  deleteContribution(contribution: Contribution): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Supprimer la cotisation',
        message: 'Êtes-vous sûr de vouloir supprimer cette cotisation ?',
        confirmText: 'Supprimer'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.contributionsService.deleteContribution(contribution.id).subscribe({
          next: () => {
            this.toastr.success('Cotisation supprimée');
            this.loadContributions();
          },
          error: () => this.toastr.error('Erreur lors de la suppression')
        });
      }
    });
  }

  applyFilter(): void {
    const filterValue = (this.searchTerm || '').toLowerCase();
    this.filteredContributions = this.contributions.filter(c =>
      c.memberName?.toLowerCase().includes(filterValue)
    );
    this.dataSource.data = this.filteredContributions;
  }

  exportToPDF(): void {
    const doc = new jsPDF();

    // Add title
    doc.setFontSize(18);
    doc.text('Liste des Cotisations', 14, 20);

    // Add date
    doc.setFontSize(11);
    doc.text(`Généré le: ${new Date().toLocaleDateString('fr-FR')}`, 14, 30);

    // Prepare data for the table
    const tableData = this.filteredContributions.map(contribution => [
      contribution.memberName || '',
      contribution.memberNumber || '',
      new Intl.NumberFormat('fr-FR', { style: 'currency', currency: 'XOF' }).format(contribution.expectedAmount),
      contribution.dueDate ? new Date(contribution.dueDate).toLocaleDateString('fr-FR') : '',
      this.getStatusText(contribution.status)
    ]);

    // Add table
    (doc as any).autoTable({
      head: [['Membre', 'Numéro', 'Montant', 'Date d\'échéance', 'Statut']],
      body: tableData,
      startY: 40,
      styles: {
        fontSize: 8,
        cellPadding: 3
      },
      headStyles: {
        fillColor: [41, 128, 185],
        textColor: 255
      },
      alternateRowStyles: {
        fillColor: [245, 245, 245]
      }
    });

    // Save the PDF
    doc.save('cotisations.pdf');
    this.toastr.success('PDF exporté avec succès');
  }

  exportToExcel(): void {
    // Prepare data for Excel
    const excelData = this.filteredContributions.map(contribution => ({
      'Membre': contribution.memberName || '',
      'Numéro': contribution.memberNumber || '',
      'Montant': contribution.expectedAmount,
      'Date d\'échéance': contribution.dueDate ? new Date(contribution.dueDate).toLocaleDateString('fr-FR') : '',
      'Statut': this.getStatusText(contribution.status)
    }));

    // Create workbook and worksheet
    const worksheet = XLSX.utils.json_to_sheet(excelData);
    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, 'Cotisations');

    // Save the file
    XLSX.writeFile(workbook, 'cotisations.xlsx');
    this.toastr.success('Excel exporté avec succès');
  }

  private getStatusText(status: string): string {
    switch (status) {
      case 'PAYE': return 'Payée';
      case 'IMPAYE': return 'Impayée';
      case 'PARTIEL': return 'Partielle';
      case 'EXONERE': return 'Exonérée';
      default: return status;
    }
  }
}
