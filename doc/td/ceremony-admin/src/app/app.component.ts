import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ThemeService } from './core/services/theme.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {
  title = 'ceremony-admin';

  constructor(private themeService: ThemeService) {}

  ngOnInit(): void {
    // Theme service is initialized here to apply saved theme on app startup
  }
}
