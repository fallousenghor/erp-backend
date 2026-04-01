import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export type Theme = 'light' | 'dark';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  private currentThemeSubject = new BehaviorSubject<Theme>('light');
  public currentTheme$ = this.currentThemeSubject.asObservable();

  private readonly THEME_KEY = 'ceremony-admin-theme';

  constructor() {
    // Load saved theme from localStorage
    const savedTheme = localStorage.getItem(this.THEME_KEY) as Theme;
    if (savedTheme && (savedTheme === 'light' || savedTheme === 'dark')) {
      this.setTheme(savedTheme);
    } else {
      // Default to light theme
      this.setTheme('light');
    }
  }

  getCurrentTheme(): Theme {
    return this.currentThemeSubject.value;
  }

  setTheme(theme: Theme): void {
    console.log('Setting theme to:', theme);
    this.currentThemeSubject.next(theme);
    localStorage.setItem(this.THEME_KEY, theme);

    // Apply theme to document
    document.documentElement.setAttribute('data-theme', theme);
    console.log('Applied data-theme attribute:', document.documentElement.getAttribute('data-theme'));
  }

  toggleTheme(): void {
    const newTheme = this.getCurrentTheme() === 'light' ? 'dark' : 'light';
    this.setTheme(newTheme);
  }
}