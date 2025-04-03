import { Injectable, Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { ErrorMessagePipe, LoggerService, NotblankValidator } from '@my/core';
import { ViewModelService } from '../code-base';
import { CategoriasDAOService, NotificationService, NavigationService } from '../common-services';
import { AuthService } from '../security';
import { FormsModule } from '@angular/forms';
import { FormButtonsComponent } from '../common-components';


@Injectable({
  providedIn: 'root'
})
// eslint-disable-next-line @typescript-eslint/no-explicit-any
export class CategoriasViewModelService extends ViewModelService<any, number> {
  constructor(dao: CategoriasDAOService, notify: NotificationService, out: LoggerService,
    auth: AuthService, router: Router, navigation: NavigationService) {
    super(dao, {}, notify, out, auth, router, navigation)
  }
  public override cancel(): void {
      this.clear()
      this.notify.clear()
      this.list()
  }
}

@Component({
  selector: 'app-categorias',
  templateUrl: './tmpl-anfitrion.component.html',
  styleUrls: ['./componente.component.css'],
  standalone: true,
  imports: [ FormsModule, RouterLink, FormButtonsComponent, ErrorMessagePipe, NotblankValidator, ]
})
export class CategoriasComponent implements OnInit {
  constructor(protected vm: CategoriasViewModelService) { }
  public get VM(): CategoriasViewModelService { return this.vm; }
  ngOnInit(): void {
    this.vm.list();
  }
}

export const CATEGORIAS_COMPONENTES = [ CategoriasComponent, ];
