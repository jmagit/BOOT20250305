import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ListButtonsComponent } from './list-buttons.component';
import { CardComponent } from './card.component';
import { FormButtonsComponent } from './form-buttons/form-buttons.component';



@NgModule({
  declarations: [],
  exports: [ ListButtonsComponent, CardComponent, FormButtonsComponent, ],
  imports: [
    CommonModule, ListButtonsComponent, CardComponent, FormButtonsComponent,
  ]
})
export class CommonComponentsModule { }
