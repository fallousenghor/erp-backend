import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../../core/services/auth.service';
import { ThemeService, Theme } from '../../../core/services/theme.service';

// Material
import { MatTabsModule } from '@angular/material/tabs';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatRadioModule } from '@angular/material/radio';
import { MatSelectModule } from '@angular/material/select';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    MatTabsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatDividerModule,
    MatRadioModule,
    MatSelectModule,
    MatSlideToggleModule
  ],
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {
  currentUser: any;
  passwordForm: FormGroup;
  currentTheme: Theme = 'light';
  selectedLanguage = 'fr';
  notificationsEnabled = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private toastr: ToastrService,
    private themeService: ThemeService
  ) {
    this.passwordForm = this.fb.group({
      currentPassword: [''],
      newPassword: [''],
      confirmPassword: ['']
    });
  }

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser();
    this.currentTheme = this.themeService.getCurrentTheme();

    // Subscribe to theme changes
    this.themeService.currentTheme$.subscribe(theme => {
      this.currentTheme = theme;
    });
  }

  editProfile(): void {
    this.toastr.info('Fonctionnalité à implémenter');
  }

  changePassword(): void {
    // TODO: Implémenter changement mot de passe
    this.toastr.info('Fonctionnalité à implémenter');
  }

  onThemeChange(theme: Theme): void {
    console.log('Theme change triggered:', theme);
    this.themeService.setTheme(theme);
    this.toastr.success(`Thème ${theme === 'dark' ? 'sombre' : 'clair'} appliqué`);
  }

  onLanguageChange(language: string): void {
    this.selectedLanguage = language;
    // TODO: Implémenter changement de langue
    this.toastr.info('Changement de langue à implémenter');
  }

  onNotificationsChange(enabled: boolean): void {
    this.notificationsEnabled = enabled;
    // TODO: Implémenter gestion des notifications
    this.toastr.info(`Notifications ${enabled ? 'activées' : 'désactivées'}`);
  }
}
