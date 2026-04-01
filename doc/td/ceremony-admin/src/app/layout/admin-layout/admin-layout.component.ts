import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule, RouterOutlet } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { ApiService } from '../../core/services/api.service';

// Material
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatDividerModule } from '@angular/material/divider';

interface MenuItem {
  label: string;
  icon: string;
  route: string;
  roles?: string[];
}

@Component({
  selector: 'app-admin-layout',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    RouterOutlet,
    MatSidenavModule,
    MatToolbarModule,
    MatIconModule,
    MatListModule,
    MatButtonModule,
    MatMenuModule,
    MatDividerModule
  ],
  templateUrl: './admin-layout.component.html',
  styleUrls: ['./admin-layout.component.scss']
})
export class AdminLayoutComponent implements OnInit {
  sidenavOpened = true;
  currentUser: any;
  activeYear: any;

  menuItems: MenuItem[] = [
    { label: 'Dashboard', icon: 'dashboard', route: '/dashboard' },
    { label: 'Utilisateurs', icon: 'people', route: '/users', roles: ['ADMIN'] },
    { label: 'Membres', icon: 'badge', route: '/members' },
    { label: 'Années', icon: 'calendar_today', route: '/years' },
    { label: 'Transactions', icon: 'account_balance', route: '/transactions' },
    { label: 'Cotisations', icon: 'payments', route: '/contributions' },
    { label: 'Événements', icon: 'event', route: '/events' },
    { label: 'Matériel', icon: 'inventory', route: '/materials' },
    { label: 'Médias', icon: 'photo_library', route: '/medias' },
    { label: 'Paramètres', icon: 'settings', route: '/settings' },
  ];

  constructor(
    public authService: AuthService,
    private router: Router,
    private api: ApiService
  ) {}

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
    });

    this.loadActiveYear();
  }

  loadActiveYear(): void {
    this.api.get('/ceremonial-years/active').subscribe({
      next: (response: any) => {
        this.activeYear = response.data;
      },
      error: () => {
        // If no active year, that's fine - just leave it null
        this.activeYear = null;
      }
    });
  }

  canShowMenuItem(item: MenuItem): boolean {
    if (!item.roles) return true;
    return this.authService.hasAnyRole(item.roles);
  }

  toggleSidebar(): void {
    this.sidenavOpened = !this.sidenavOpened;
  }

  logout(): void {
    this.authService.logout();
  }
}
