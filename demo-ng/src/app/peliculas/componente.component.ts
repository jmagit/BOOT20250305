/* eslint-disable @typescript-eslint/no-unused-vars */
/* eslint-disable @typescript-eslint/no-empty-function */
/* eslint-disable @angular-eslint/no-empty-lifecycle-method */
import { Component, OnInit, OnDestroy, OnChanges, SimpleChanges, forwardRef, input } from '@angular/core';
import { ActivatedRoute, Router, ParamMap, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { DatePipe, NgIf, } from '@angular/common';
import { PaginatorModule } from 'primeng/paginator';
import { ErrorMessagePipe, TypeValidator } from '@my/core';
import { PeliculasViewModelService } from './servicios.service';
import { Subscription } from 'rxjs';

@Component({
    selector: 'app-peliculas',
    templateUrl: './tmpl-anfitrion.component.html',
    styleUrls: ['./componente.component.css'],
    imports: [
        forwardRef(() => PeliculasAddComponent),
        forwardRef(() => PeliculasEditComponent),
        forwardRef(() => PeliculasViewComponent),
        forwardRef(() => PeliculasListComponent),
    ]
})
export class PeliculasComponent implements OnInit, OnDestroy {
  constructor(protected vm: PeliculasViewModelService) { }
  public get VM(): PeliculasViewModelService { return this.vm; }
  ngOnInit(): void {
    // this.vm.list();
    this.vm.load()
  }
  ngOnDestroy(): void { this.vm.clear(); }
}

/*
@Component({
  selector: 'app-peliculas-list',
  templateUrl: './tmpl-list.sin-rutas.component.html',
  styleUrls: ['./componente.component.css'],
  standalone: true,
  imports: [PaginatorModule, NgIf, ]
})
export class PeliculasListComponent implements OnInit, OnDestroy {
  constructor(protected vm: PeliculasViewModelService) { }
  public get VM(): PeliculasViewModelService { return this.vm; }
  ngOnInit(): void { }
  ngOnDestroy(): void { }
}
@Component({
  selector: 'app-peliculas-add',
  templateUrl: './tmpl-form.component.html',
  styleUrls: ['./componente.component.css'],
  standalone: true,
  imports: [FormsModule, TypeValidator, ErrorMessagePipe]
})
export class PeliculasAddComponent implements OnInit {
  constructor(protected vm: PeliculasViewModelService) { }
  public get VM(): PeliculasViewModelService { return this.vm; }
  ngOnInit(): void { }
}
@Component({
  selector: 'app-peliculas-edit',
  templateUrl: './tmpl-form.component.html',
  styleUrls: ['./componente.component.css'],
  standalone: true,
  imports: [FormsModule, TypeValidator, ErrorMessagePipe]
})
export class PeliculasEditComponent implements OnInit, OnDestroy {
  constructor(protected vm: PeliculasViewModelService) { }
  public get VM(): PeliculasViewModelService { return this.vm; }
  ngOnInit(): void { }
  ngOnDestroy(): void { }
}
@Component({
  selector: 'app-peliculas-view',
  templateUrl: './tmpl-view.component.html',
  styleUrls: ['./componente.component.css'],
  standalone: true,
  imports: [DatePipe]
})
export class PeliculasViewComponent implements OnInit, OnDestroy {
  constructor(protected vm: PeliculasViewModelService) { }
  public get VM(): PeliculasViewModelService { return this.vm; }
  ngOnInit(): void { }
  ngOnDestroy(): void { }
}
*/

@Component({
    selector: 'app-peliculas-list',
    templateUrl: './tmpl-list.con-rutas.component.html',
    styleUrls: ['./componente.component.css'],
    imports: [RouterLink, PaginatorModule]
})
export class PeliculasListComponent implements OnChanges, OnDestroy {
  readonly page = input(0);

  constructor(protected vm: PeliculasViewModelService) { }
  public get VM(): PeliculasViewModelService { return this.vm; }
  // ngOnInit(): void {
  //   // this.vm.list();
  //   this.vm.load()
  // }
  ngOnChanges(_changes: SimpleChanges): void {
    this.vm.load(this.page())
  }
  ngOnDestroy(): void { this.vm.clear(); }
}
@Component({
    selector: 'app-peliculas-add',
    templateUrl: './tmpl-form.component.html',
    styleUrls: ['./componente.component.css'],
    imports: [FormsModule, TypeValidator, ErrorMessagePipe]
})
export class PeliculasAddComponent implements OnInit {
  constructor(protected vm: PeliculasViewModelService) { }
  public get VM(): PeliculasViewModelService { return this.vm; }
  ngOnInit(): void {
    this.vm.add();
  }
}
@Component({
    selector: 'app-peliculas-edit',
    templateUrl: './tmpl-form.component.html',
    styleUrls: ['./componente.component.css'],
    imports: [FormsModule, TypeValidator, ErrorMessagePipe]
})
export class PeliculasEditComponent implements OnInit, OnDestroy {
  private obs$?: Subscription;
  constructor(protected vm: PeliculasViewModelService,
    protected route: ActivatedRoute, protected router: Router) { }
  public get VM(): PeliculasViewModelService { return this.vm; }
  ngOnInit(): void {
    this.obs$ = this.route.paramMap.subscribe(
      (params: ParamMap) => {
        const id = parseInt(params?.get('id') ?? '');
        if (id) {
          this.vm.edit(id);
        } else {
          this.router.navigate(['/404.html']);
        }
      });
  }
  ngOnDestroy(): void {
    this.obs$!.unsubscribe();
  }
}
@Component({
    selector: 'app-peliculas-view',
    templateUrl: './tmpl-view.component.html',
    styleUrls: ['./componente.component.css'],
    imports: [DatePipe]
})
export class PeliculasViewComponent implements OnChanges {
  readonly id = input<string>();
  constructor(protected vm: PeliculasViewModelService, protected router: Router) { }
  public get VM(): PeliculasViewModelService { return this.vm; }
  ngOnChanges(_changes: SimpleChanges): void {
    const id = this.id();
    if (id) {
      this.vm.view(+id);
    } else {
      this.router.navigate(['/404.html']);
    }
  }
}


export const PELICULAS_COMPONENTES = [
  // PeliculasComponent,
  PeliculasListComponent, PeliculasAddComponent, PeliculasEditComponent, PeliculasViewComponent,
];
