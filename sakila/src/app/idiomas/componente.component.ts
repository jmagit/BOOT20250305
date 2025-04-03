import { Injectable, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { LoggerService, ErrorMessagePipe, NotblankValidator } from '@my/core';
import { ViewModelService } from '../code-base';
import { FormButtonsComponent } from '../common-components';
import { IdiomasDAOService, NotificationService, NavigationService } from '../common-services';
import { AuthService } from '../security';

@Injectable({
  providedIn: 'root'
})
// eslint-disable-next-line @typescript-eslint/no-explicit-any
export class IdiomasViewModelService extends ViewModelService<any, number> {
  constructor(dao: IdiomasDAOService, notify: NotificationService, out: LoggerService,
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
  selector: 'app-idiomas',
  templateUrl: './tmpl-anfitrion.component.html',
  styleUrls: ['./componente.component.css'],
  standalone: true,
  imports: [FormsModule, FormButtonsComponent, ErrorMessagePipe, NotblankValidator,],
})
export class IdiomasComponent implements OnInit {
  constructor(protected vm: IdiomasViewModelService) { }
  public get VM(): IdiomasViewModelService { return this.vm; }
  ngOnInit(): void {
    this.vm.list();
  }
}

export const IDIOMAS_COMPONENTES = [ IdiomasComponent, ];
