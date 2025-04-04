/* eslint-disable @typescript-eslint/no-explicit-any */
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, fakeAsync, inject, TestBed, tick } from '@angular/core/testing';
import { LoggerService } from '@my/core';
import { ActoresDAOService, NavigationService, NotificationService } from '../common-services';

import { NO_ERRORS_SCHEMA, Type } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ACTORES_COMPONENTES, ActoresViewModelService } from './componente.component';
import { environment } from 'src/environments/environment';
import { DAOServiceMock } from '../code-base';
import { provideLocationMocks } from '@angular/common/testing';
import { provideRouter } from '@angular/router';
import { Observable, of } from 'rxjs';
export interface Actor {
  id: number
  nombre: string
  apellidos: string
}

describe('Modulo Actores', () => {
  const apiURL = environment.apiURL + 'catalogo/actores/v1'
  const dataMock = [
    { "id": 1, "nombre": "Marline", "apellidos": "Lockton Jerrans", },
    { "id": 2, "nombre": "Beale", "apellidos": "Knibb Koppe", },
    { "id": 3, "nombre": "Gwenora", "apellidos": "Forrestor Fitzackerley", },
    { "id": 4, "nombre": "Umberto", "apellidos": "Langforth Spenclay", }
  ];
  const dataAddMock: Record<string, any> = { id: 0, nombre: "Pepito", apellido: "Grillo" }
  const dataEditMock: Record<string, any> = { id: 1, nombre: "Pepito", apellido: "Grillo" }
  const dataBadMock: Record<string, any> = { id: -1 }

  describe('DAOService', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
    imports: [],
          providers: [ActoresDAOService, provideHttpClient(), provideHttpClientTesting()]
      });
    });

    it('query', inject([ActoresDAOService, HttpTestingController], (dao: ActoresDAOService, httpMock: HttpTestingController) => {
      dao.query().subscribe({
          next: data => {
            expect(data.length).toEqual(dataMock.length);
          },
          error: () => { fail('has executed "error" callback'); }
        });
      const req = httpMock.expectOne(apiURL + '?modo=short');
      expect(req.request.method).toEqual('GET');
      req.flush([...dataMock]);
      httpMock.verify();
    }));

    it('get', inject([ActoresDAOService, HttpTestingController], (dao: ActoresDAOService, httpMock: HttpTestingController) => {
      dao.get(1).subscribe({
          next: data => {
            expect(data).toEqual(dataMock[0]);
          },
          error: () => { fail('has executed "error" callback'); }
        });
      const req = httpMock.expectOne(`${apiURL}/1`);
      expect(req.request.method).toEqual('GET');
      req.flush({ ...dataMock[0] });
      httpMock.verify();
    }));

    it('add', inject([ActoresDAOService, HttpTestingController], (dao: ActoresDAOService, httpMock: HttpTestingController) => {
      const item = { ...dataAddMock };
      dao.add(item).subscribe();
      const req = httpMock.expectOne(`${apiURL}`);
      expect(req.request.method).toEqual('POST');
      for (const key in dataEditMock) {
        if (Object.prototype.hasOwnProperty.call(dataAddMock, key)) {
          expect(req.request.body[key]).toEqual(dataAddMock[key]);
        }
      }
      httpMock.verify();
    }));

    it('change', inject([ActoresDAOService, HttpTestingController], (dao: ActoresDAOService, httpMock: HttpTestingController) => {
      const item = { ...dataEditMock };
      dao.change(1, item).subscribe();
      const req = httpMock.expectOne(`${apiURL}/1`);
      expect(req.request.method).toEqual('PUT');
      for (const key in dataEditMock) {
        if (Object.prototype.hasOwnProperty.call(dataEditMock, key)) {
          expect(req.request.body[key]).toEqual(dataEditMock[key]);
        }
      }
      httpMock.verify();
    }));

    it('delete', inject([ActoresDAOService, HttpTestingController], (dao: ActoresDAOService, httpMock: HttpTestingController) => {
      dao.remove(1).subscribe();
      const req = httpMock.expectOne(`${apiURL}/1`);
      expect(req.request.method).toEqual('DELETE');
      httpMock.verify();
    }));

  });
  class ActoresDAOServiceMock extends DAOServiceMock<any, number> {
    constructor(listado: Actor[]) {
      super(listado);
    }
    peliculas(_id: number): Observable<any[]> {
      return of([]);
    }
  }
  describe('ViewModelService', () => {
    let service: ActoresViewModelService;
    let dao: ActoresDAOService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        providers: [NotificationService, LoggerService,
              provideHttpClient(withInterceptorsFromDi()), provideHttpClientTesting(),
              provideRouter([]), provideLocationMocks(),
                { provide: ActoresDAOService, useFactory: () => new ActoresDAOServiceMock([...dataMock]) }]
        });
      service = TestBed.inject(ActoresViewModelService);
      dao = TestBed.inject(ActoresDAOService);
    });

    it('should be created', () => {
      expect(service).toBeTruthy();
    });

    describe('mode', () => {
      it('list', fakeAsync(() => {
        service.list()
        tick()
        expect(service.Listado.length).withContext('Verify Listado length').toBe(dataMock.length)
        expect(service.Modo).withContext('Verify Modo is ').toBe('list')
      }))

      it('add', () => {
        service.add()
        expect(service.Elemento).withContext('Verify Elemento').toEqual({})
        expect(service.Modo).withContext('Verify Modo is add').toBe('add')
      })

      describe('edit', () => {
        it(' OK', fakeAsync(() => {
          service.edit(3)
          tick()

          expect(service.Elemento).withContext('Verify Elemento').toEqual(dataMock[2])
          expect(service.Modo).withContext('Verify Modo is edit').toBe('edit')
        }))

        it('KO', fakeAsync(() => {
          const notify = TestBed.inject(NotificationService);
          spyOn(notify, 'add')

          service.edit(dataMock.length + 1)
          tick()

          expect(notify.add).withContext('notify error').toHaveBeenCalled()
        }))
      })

      describe('view', () => {
        it(' OK', fakeAsync(() => {
          service.view(1)
          tick()

          expect(service.Elemento).withContext('Verify Elemento').toEqual(dataMock[0])
          expect(service.Modo).withContext('Verify Modo is view').toBe('view')
        }))

        it('KO', fakeAsync(() => {
          const notify = TestBed.inject(NotificationService);
          spyOn(notify, 'add')

          service.view(dataMock.length + 1)
          tick()

          expect(notify.add).withContext('notify error').toHaveBeenCalled()
        }))
      })

      describe('delete', () => {
        it('accept confirm', fakeAsync(() => {
          spyOn(window, 'confirm').and.returnValue(true)
          service.list()
          tick()
          service.delete(3)
          tick()
          expect(service.Listado.length).withContext('Verify Listado length').toBe(dataMock.length - 1)
          expect(service.Modo).withContext('Verify Modo is list').toBe('list')
        }))

        it('reject confirm', fakeAsync(() => {
          spyOn(window, 'confirm').and.returnValue(false)
          service.list()
          tick()
          service.delete(+1)
          tick()
          expect((dao as Record<string, any>)['listado'].length).withContext('Verify Listado length').toBe(dataMock.length)
        }))

        it('KO', fakeAsync(() => {
          spyOn(window, 'confirm').and.returnValue(true)
          const notify = TestBed.inject(NotificationService);
          spyOn(notify, 'add')

          service.delete(dataMock.length + 1)
          tick()

          expect(notify.add).withContext('notify error').toHaveBeenCalled()
        }))
      })
    })

    it('cancel', fakeAsync(() => {
      const navigation = TestBed.inject(NavigationService);
      spyOn(navigation, 'back')
      service.edit(2)
      tick()
      expect(service.Elemento).withContext('Verifica fase de preparación').not.toEqual({})
      service.cancel()
      expect(service.Elemento).withContext('Verify Elemento').toEqual({})
      expect(navigation.back).toHaveBeenCalled()
    }))

    describe('send', () => {
      describe('add', () => {
        it('OK', fakeAsync(() => {
          spyOn(service, 'cancel')
          service.add()
          tick()
          for (const key in dataAddMock) {
            service.Elemento[key] = dataAddMock[key];
          }
          service.send()
          tick()
          const listado = (dao as Record<string, any>)['listado']
          expect(listado.length).toBe(dataMock.length + 1)
          expect(listado[listado.length - 1]).toEqual(dataAddMock)
          expect(service.cancel).withContext('Verify init ViewModel').toHaveBeenCalled()
        }))
        it('KO', fakeAsync(() => {
          const notify = TestBed.inject(NotificationService);
          spyOn(notify, 'add')
          service.add()
          tick()
          for (const key in dataBadMock) {
            service.Elemento[key] = dataBadMock[key];
          }
          service.send()
          tick()
          expect(notify.add).withContext('notify error').toHaveBeenCalled()
        }))
      })

      describe('edit', () => {
        it('OK', fakeAsync(() => {
          spyOn(service, 'cancel')
          service.edit(1)
          tick()
          for (const key in dataEditMock) {
            service.Elemento[key] = dataEditMock[key];
          }
          service.send()
          tick()
          const listado = (dao as Record<string, any>)['listado']
          expect(listado.length).withContext('Verify Listado length').toBe(dataMock.length)
          expect(listado[0]).withContext('Verify Elemento').toEqual(service.Elemento)
          expect(service.cancel).withContext('Verify init ViewModel').toHaveBeenCalled()
        }))
        it('KO', fakeAsync(() => {
          const notify = TestBed.inject(NotificationService);
          spyOn(notify, 'add')
          service.edit(1)
          tick()
          for (const key in dataBadMock) {
            service.Elemento[key] = dataBadMock[key];
          }
          (dao as Record<string, any>)['listado'].splice(0)
          service.send()
          tick()
          expect(notify.add).withContext('notify error').toHaveBeenCalled()
        }))
      })
    })

  });
  describe('Componentes', () => {
    ACTORES_COMPONENTES.forEach(componente => {
      describe(componente.name, () => {
        let component: any;
        let fixture: ComponentFixture<any>;

        beforeEach(async () => {
          await TestBed.configureTestingModule({
              // declarations: [componente],
              schemas: [NO_ERRORS_SCHEMA],
              imports: [FormsModule, componente],
              providers: [NotificationService, LoggerService,
                provideHttpClient(withInterceptorsFromDi()), provideHttpClientTesting(),
                provideRouter([]), provideLocationMocks()]
          })
            .compileComponents();
        });

        beforeEach(() => {
          fixture = TestBed.createComponent(componente as Type<any>);
          component = fixture.componentInstance;
          fixture.detectChanges();
        });

        it('should create', () => {
          expect(component).toBeTruthy();
        });
      });

    })
  })
});
