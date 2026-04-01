import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'currencyXof'
})
export class CurrencyXofPipe implements PipeTransform {
  transform(value: number): string {
    return new Intl.NumberFormat('fr-FR', {
      style: 'currency',
      currency: 'XOF'
    }).format(value);
  }
}