import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { ToastrService } from 'ngx-toastr';
import { MembersService } from '../members.service';

@Component({
  selector: 'app-member-detail',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatChipsModule,
    MatDividerModule
  ],
  templateUrl: './member-detail.component.html',
  styleUrls: ['./member-detail.component.scss']
})
export class MemberDetailComponent implements OnInit {
  member: any = null;
  loading = true;
  contributions: any[] = [];
  payments: any[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private membersService: MembersService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    const memberId = this.route.snapshot.params['id'];
    this.loadMemberDetails(memberId);
  }

  loadMemberDetails(id: number): void {
    this.loading = true;
    this.membersService.getMemberById(id).subscribe({
      next: (response) => {
        this.member = response.data;
        this.loading = false;
        // Charger les cotisations et paiements
        this.loadContributions(id);
        this.loadPayments(id);
      },
      error: () => {
        this.toastr.error('Erreur lors du chargement');
        this.loading = false;
      }
    });
  }

  loadContributions(memberId: number): void {
    // TODO: Implémenter avec le service contributions
    this.contributions = [];
  }

  loadPayments(memberId: number): void {
    // TODO: Implémenter avec le service payments
    this.payments = [];
  }

  editMember(): void {
    this.router.navigate(['/members/edit', this.member.id]);
  }

  downloadBadge(): void {
    if (this.member.badge?.pdfUrl) {
      window.open(this.member.badge.pdfUrl, '_blank');
    } else {
      this.toastr.info('Badge non disponible');
    }
  }

  back(): void {
    this.router.navigate(['/members']);
  }
}