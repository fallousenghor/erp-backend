import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatPaginatorModule, MatPaginator } from '@angular/material/paginator';
import { MatSortModule, MatSort } from '@angular/material/sort';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatChipsModule } from '@angular/material/chips';
import { Router } from '@angular/router';
import { MembersService, Member } from '../members.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-members-list',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule,
    MatDialogModule,
    MatCardModule,
    MatProgressSpinnerModule,
    MatChipsModule
  ],
  templateUrl: './member-list.component.html',
  styleUrls: ['./member-list.component.scss']
})
export class MembersListComponent implements OnInit {
  displayedColumns: string[] = ['memberNumber', 'fullName', 'phoneNumber', 'email', 'active', 'actions'];
  dataSource: MatTableDataSource<Member>;
  loading = true;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private membersService: MembersService,
    private router: Router,
    private dialog: MatDialog,
    private toastr: ToastrService
  ) {
    this.dataSource = new MatTableDataSource<Member>([]);
  }

  ngOnInit(): void {
    this.loadMembers();
  }

  loadMembers(): void {
    this.loading = true;
    this.membersService.getMembers().subscribe({
      next: (response) => {
        this.dataSource.data = response.data;
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
        this.loading = false;
      },
      error: (error) => {
        this.toastr.error('Erreur de chargement des membres');
        this.loading = false;
      }
    });
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  viewMember(member: Member): void {
    this.router.navigate(['/members', member.id]);
  }

  editMember(member: Member): void {
    this.router.navigate(['/members/edit', member.id]);
  }

  deleteMember(member: Member): void {
    if (confirm(`Supprimer le membre ${member.firstName} ${member.lastName} ?`)) {
      this.membersService.deleteMember(member.id).subscribe({
        next: () => {
          this.toastr.success('Membre supprimé avec succès');
          this.loadMembers();
        },
        error: () => {
          this.toastr.error('Erreur lors de la suppression');
        }
      });
    }
  }

  addMember(): void {
    this.router.navigate(['/members/new']);
  }
}