import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ACTORES_COMPONENTES, ActoresAddComponent, ActoresEditComponent, ActoresListComponent, ActoresViewComponent } from './componente.component';
import { environment } from 'src/environments/environment';
import { AuthWithRedirectCanActivate, InRoleCanActivate } from '../security';

export const routes: Routes = [
  { path: '', component: ActoresListComponent },
  { path: 'add', component: ActoresAddComponent,
    canActivate: [AuthWithRedirectCanActivate('/login'), InRoleCanActivate(environment.roleMantenimiento)] },
  { path: ':id/edit', component: ActoresEditComponent,
    canActivate: [AuthWithRedirectCanActivate('/login'), InRoleCanActivate(environment.roleMantenimiento)] },
  { path: ':id', component: ActoresViewComponent },
  { path: ':id/:kk', component: ActoresViewComponent },
];

@NgModule({
  declarations: [],
  exports: [
    RouterModule
  ],
  imports: [
    RouterModule.forChild(routes), ACTORES_COMPONENTES,
  ]
})
export default class ActoresModule { }
