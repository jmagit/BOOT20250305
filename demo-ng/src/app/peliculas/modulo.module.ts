import { NgModule } from '@angular/core';
import { PELICULAS_COMPONENTES, PeliculasAddComponent, PeliculasComponent, PeliculasEditComponent, PeliculasListComponent, PeliculasViewComponent } from './componente.component';
import { RouterModule, Routes } from '@angular/router';
import { InRoleCanActivate } from '../security';

export const routes: Routes = [
  { path: '', component: PeliculasListComponent },
  { path: 'add', component: PeliculasAddComponent, canActivate: [ InRoleCanActivate('Administradores')] },
  { path: ':id/edit', component: PeliculasEditComponent },
  { path: ':id', component: PeliculasViewComponent },
  { path: ':id/:kk', component: PeliculasViewComponent },
]
@NgModule({
  declarations: [],
  imports: [ PeliculasComponent, PELICULAS_COMPONENTES, RouterModule.forChild(routes) ],
  exports: [ PeliculasComponent, RouterModule ]
})
export class PeliculasModule { }
