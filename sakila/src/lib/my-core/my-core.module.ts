import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PIPES_CADENAS } from './pipes/cadenas.pipe';
import { SizerComponent } from './components/sizer.component';
import { PIPES_NUMERICOS } from './pipes/numericos.pipe';
import { MIS_VALIDADORES } from './directives/mis-validadores.directive';
import { VALIDATORS_DATES } from './directives/dates-validators.directive';



@NgModule({
  declarations: [ ],
  imports: [
    CommonModule, PIPES_CADENAS, PIPES_NUMERICOS, MIS_VALIDADORES, VALIDATORS_DATES, SizerComponent,
  ],
  exports: [ PIPES_CADENAS, PIPES_NUMERICOS, MIS_VALIDADORES, VALIDATORS_DATES, SizerComponent, ],
})
export class MyCoreModule { }
