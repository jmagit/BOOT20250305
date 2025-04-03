import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CategoriasComponent } from './componente.component';

export const routes: Routes = [
  { path: '', component: CategoriasComponent },
];

@NgModule({
  declarations: [ ],
  exports: [
    RouterModule
  ],
  imports: [
    RouterModule.forChild(routes), CategoriasComponent,
  ]
})
export default class CategoriasModule { }
