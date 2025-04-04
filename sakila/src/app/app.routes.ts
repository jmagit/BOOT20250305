import { Routes } from '@angular/router';
import { HomeComponent, PageNotFoundComponent } from './main';
import { environment } from 'src/environments/environment';
import { PeliculasListComponent, routes as PeliculasRoutes } from './peliculas';
import { routes as ActoresRoutes } from './actores';
import { AuthWithRedirectCanActivate, InRoleCanActivate, AuthCanActivateFn, LoginFormComponent, RegisterUserComponent } from './security';

export const routes: Routes = [
  { path: '', pathMatch: 'full', component: HomeComponent },
  { path: 'inicio', component: HomeComponent },
  { path: 'catalogo/categorias', component: PeliculasListComponent, data: { search: 'categorias' }, title: 'categorias' },
  { path: 'catalogo/categorias/:idPeli/:tit', redirectTo: '/catalogo/:idPeli/:tit', title: 'catalogo' },
  { path: 'catalogo', children: PeliculasRoutes, title: 'catalogo' },
  { path: 'actores/:id/:nom/:idPeli/:tit', redirectTo: '/catalogo/:idPeli/:tit', title: 'catalogo' },
  {
    path: 'actores', children: ActoresRoutes, title: 'actores'
  },
  {
    path: 'categorias', loadChildren: () => import('./categorias/modulo.module'), title: 'categorias',
    canActivate: [AuthWithRedirectCanActivate('/login'), InRoleCanActivate(environment.roleMantenimiento)]
  },
  {
    path: 'idiomas', loadChildren: () => import('./idiomas/modulo.module'), title: 'idiomas',
    canActivate: [AuthWithRedirectCanActivate('/login'), InRoleCanActivate(environment.roleMantenimiento)]
  },
  {
    path: 'contactos', loadChildren: () => import('./contactos/modulo.module').then(mod => mod.ContactosModule), title: 'contactos',
    canActivate: [AuthCanActivateFn]
  },

  { path: 'login', component: LoginFormComponent },
  { path: 'registro', component: RegisterUserComponent },

  { path: '404.html', component: PageNotFoundComponent },
  { path: '**', component: PageNotFoundComponent },
];
