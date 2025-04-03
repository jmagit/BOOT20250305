import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PELICULAS_COMPONENTES, PeliculasAddComponent, PeliculasEditComponent, PeliculasViewComponent, PeliculasListComponent } from './componente.component';
import { AuthWithRedirectCanActivate, InRoleCanActivate } from '../security';
import { environment } from '../../environments/environment';

export const routes: Routes = [
  { path: '', component: PeliculasListComponent },
  {
    path: 'add', component: PeliculasAddComponent,
    canActivate: [AuthWithRedirectCanActivate('/login'), InRoleCanActivate(environment.roleMantenimiento)]
  },
  {
    path: ':id/edit', component: PeliculasEditComponent,
    canActivate: [AuthWithRedirectCanActivate('/login'), InRoleCanActivate(environment.roleMantenimiento)]
  },
  { path: ':id', component: PeliculasViewComponent },
  { path: ':id/:kk', component: PeliculasViewComponent },
];

@NgModule({
  declarations: [ ],
  exports: [
    PELICULAS_COMPONENTES,
    RouterModule,
  ],
  imports: [
    RouterModule.forChild(routes), PELICULAS_COMPONENTES,
  ]
})
export class PeliculasModule { }
