import { Routes, UrlSegment } from '@angular/router';
import { HomeComponent, PageNotFoundComponent } from './main';
import { DemosComponent } from './ejemplos';
import { FormulariosComponent } from './ejemplos/formularios/formularios.component';
import { LoginFormComponent, RegisterUserComponent } from './security';

export function graficoFiles(url: UrlSegment[]) {
  return url.length === 1 && url[0].path.endsWith('.svg') ? ({consumed: url}) : null;
}

export const routes: Routes = [
  {path: '', component: HomeComponent, pathMatch: 'full' },
  {path: 'inicio', component: HomeComponent, },
  {path: 'demos', component: DemosComponent, title: 'Demostración'},
  {path: 'esto/es/un/formulario', component: FormulariosComponent},
  {path: 'personas', component: FormulariosComponent},
  {path: 'personas/add', component: FormulariosComponent},
  {path: 'personas/:id/edit', component: FormulariosComponent},
  {path: 'personas/:id', component: FormulariosComponent},
  {path: 'personas/:id/:kk', component: FormulariosComponent},
  {path: 'pepito/grillo', redirectTo: '/persona/2'},
  {path: 'libros', children: [
    {path: '', component: FormulariosComponent},
    {path: 'add', component: FormulariosComponent},
    {path: ':id/edit', component: FormulariosComponent},
    {path: ':id', component: FormulariosComponent},
    {path: ':id/:kk', component: FormulariosComponent},
  ]},
  { path: 'login', component: LoginFormComponent },
  { path: 'registro', component: RegisterUserComponent },
  {matcher: graficoFiles, loadComponent: () => import('./ejemplos/grafico-svg/grafico-svg.component') },
  {path: 'config', loadChildren: () => import('./config/config.module') },
  {path: '404.html', component: PageNotFoundComponent},
  {path: '**', component: PageNotFoundComponent}
];
