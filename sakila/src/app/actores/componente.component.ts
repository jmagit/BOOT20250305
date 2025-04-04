/* eslint-disable @typescript-eslint/no-explicit-any */

import { CommonModule } from '@angular/common';
import { Injectable, Component, OnChanges, OnDestroy, Input, SimpleChanges, OnInit, effect, input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { LoggerService, ErrorMessagePipe, NormalizePipe, NotblankValidator, UppercaseValidator, TypeValidator } from '@my/core';
import { PaginatorModule } from 'primeng/paginator';
import { ViewModelService } from '../code-base';
import { FormButtonsComponent } from '../common-components';
import { ActoresDAOService, NotificationService, NavigationService } from '../common-services';
import { PeliculasListBodyComponent } from '../peliculas';
import { AuthService } from '../security';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ActoresViewModelService extends ViewModelService<any, number> {
  constructor(dao: ActoresDAOService, notify: NotificationService, out: LoggerService,
    auth: AuthService, router: Router, navigation: NavigationService) {
    super(dao, {}, notify, out, auth, router, navigation)
  }
  page = 0;
  totalPages = 0;
  totalRows = 0;
  rowsPerPage = 10;
  load(page: number = -1) {
    if (!page || page < 0) page = this.page;
    (this.dao as ActoresDAOService).page(page, this.rowsPerPage).subscribe({
      next: rslt => {
        this.page = rslt.page;
        this.totalPages = rslt.pages;
        this.totalRows = rslt.rows;
        this.listado = rslt.list;
        this.modo = 'list';
      },
      error: err => this.handleError(err)
    })
  }

  peliculas: any[] = []

  public override view(key: number): void {
    super.view(key);
    (this.dao as ActoresDAOService).peliculas(key).subscribe({
      next: data => {
        this.peliculas = data //.map(item => ({filmId: item.key, title: item.value }));
      },
      error: err => this.handleError(err)
    });
  }
}

@Component({
  selector: 'app-actores-list',
  templateUrl: './tmpl-list.component.html',
  styleUrls: ['./componente.component.css'],
  standalone: true,
  imports: [RouterLink, PaginatorModule, CommonModule, NormalizePipe, ]
})
export class ActoresListComponent implements OnDestroy {
  readonly roleMantenimiento = environment.roleMantenimiento
  readonly page = input(0);

  constructor(protected vm: ActoresViewModelService) {
    effect(() => vm.load(this.page()))
  }

  public get VM(): ActoresViewModelService { return this.vm; }

  ngOnDestroy(): void { this.vm.clear(); }
}

@Component({
  selector: 'app-actores-add',
  templateUrl: './tmpl-form.component.html',
  styleUrls: ['./componente.component.css'],
  standalone: true,
  imports: [FormsModule, CommonModule, ErrorMessagePipe, NotblankValidator, UppercaseValidator, TypeValidator, FormButtonsComponent]
})
export class ActoresAddComponent implements OnInit {
  constructor(protected vm: ActoresViewModelService) { }
  public get VM(): ActoresViewModelService { return this.vm; }
  ngOnInit(): void {
    this.vm.add();
  }
}

@Component({
  selector: 'app-actores-edit',
  templateUrl: './tmpl-form.component.html',
  styleUrls: ['./componente.component.css'],
  standalone: true,
  imports: [FormsModule, CommonModule, ErrorMessagePipe, NotblankValidator, UppercaseValidator, TypeValidator, FormButtonsComponent]
})
export class ActoresEditComponent implements OnChanges {
  @Input() id?: string;
  constructor(protected vm: ActoresViewModelService, protected router: Router) { }
  public get VM(): ActoresViewModelService { return this.vm; }
  ngOnChanges(_changes: SimpleChanges): void {
    if (this.id) {
      this.vm.edit(+this.id);
    } else {
      this.router.navigate(['/404.html']);
    }
  }
}

@Component({
  selector: 'app-actores-view',
  templateUrl: './tmpl-view.component.html',
  styleUrls: ['./componente.component.css'],
  standalone: true,
  imports: [ FormButtonsComponent, PeliculasListBodyComponent, ]
})
export class ActoresViewComponent {
  constructor(protected vm: ActoresViewModelService, protected router: Router) { }
  public get VM(): ActoresViewModelService { return this.vm; }
  @Input() set id(key: string) {
    if (+key) {
      this.vm.view(+key);
    } else {
      this.router.navigate(['/404.html']);
    }
  }
}


export const ACTORES_COMPONENTES = [ActoresListComponent, ActoresAddComponent, ActoresEditComponent, ActoresViewComponent,];
