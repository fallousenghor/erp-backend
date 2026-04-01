import { Component, Input, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, MatPaginator } from '@angular/material/paginator';

@Component({
  selector: 'app-table',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatPaginatorModule],
  templateUrl: './table.component.html',
  styleUrl: './table.component.scss'
})
export class TableComponent {
  @Input() dataSource: any[] = [];
  @Input() columns: string[] = [];
  @Input() hasActions = false;
  @Input() pageSizeOptions = [10, 25, 50];
  @Input() paginator = false;
  
  @ViewChild(MatPaginator) paginatorRef!: MatPaginator;

  get displayColumns(): string[] {
    return this.hasActions ? [...this.columns, 'actions'] : this.columns;
  }
}
