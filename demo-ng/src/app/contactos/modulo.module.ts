import { NgModule } from '@angular/core';
import { CONTACTOS_COMPONENTES, ContactosAddComponent, ContactosComponent, ContactosEditComponent, ContactosListComponent, ContactosViewComponent } from './componente.component';
import { RouterModule, Routes } from '@angular/router';
import { InRoleCanActivate } from '../security';

export const routes: Routes = [
  { path: '', component: ContactosListComponent },
  { path: 'add', component: ContactosAddComponent, canActivate: [ InRoleCanActivate('Administradores')] },
  { path: ':id/edit', component: ContactosEditComponent },
  { path: ':id', component: ContactosViewComponent },
  { path: ':id/:kk', component: ContactosViewComponent },
]
@NgModule({
  declarations: [],
  imports: [ ContactosComponent, CONTACTOS_COMPONENTES, RouterModule.forChild(routes) ],
  exports: [ ContactosComponent, RouterModule ]
})
export class ContactosModule { }
