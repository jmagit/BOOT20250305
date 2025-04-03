import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { IdiomasComponent } from './componente.component';

export const routes: Routes = [
  { path: '', component: IdiomasComponent },
];

@NgModule({
  declarations: [ ],
  exports: [
    RouterModule
  ],
  imports: [
    RouterModule.forChild(routes), IdiomasComponent,
  ]
})
export default class IdiomasModule { }
