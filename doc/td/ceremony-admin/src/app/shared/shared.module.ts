import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CurrencyXofPipe } from './pipes/currency-xof.pipe';
import { PhoneFormatPipe } from './pipes/phone-format.pipe';
import { TimeAgoPipe } from './pipes/time-ago.pipe';

// Composants partagés
import { ConfirmDialogComponent } from './components/confirm-dialog/confirm-dialog.component';
import { LoadingSpinnerComponent } from './components/loading-spinner/loading-spinner.component';

@NgModule({
  declarations: [
    CurrencyXofPipe,
    PhoneFormatPipe,
    TimeAgoPipe,
    ConfirmDialogComponent,
    LoadingSpinnerComponent
  ],
  imports: [
    CommonModule
  ],
  exports: [
    CurrencyXofPipe,
    PhoneFormatPipe,
    TimeAgoPipe,
    ConfirmDialogComponent,
    LoadingSpinnerComponent
  ]
})
export class SharedModule { }