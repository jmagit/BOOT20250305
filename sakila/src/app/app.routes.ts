import { Routes, UrlSegment } from '@angular/router';
import { HomeComponent, PageNotFoundComponent } from './main';
import { LoginFormComponent, RegisterUserComponent } from './security';

export function graficoFiles(url: UrlSegment[]) {
  return url.length === 1 && url[0].path.endsWith('.svg') ? ({consumed: url}) : null;
}

export const routes: Routes = [
  {path: '', component: HomeComponent, pathMatch: 'full' },
  {path: 'inicio', component: HomeComponent, },

  { path: 'login', component: LoginFormComponent },
  { path: 'registro', component: RegisterUserComponent },
  { path: '404.html', component: PageNotFoundComponent },
  { path: '**', component: PageNotFoundComponent }
];
