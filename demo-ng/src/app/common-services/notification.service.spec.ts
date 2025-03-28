import { TestBed } from '@angular/core/testing';
import { LoggerService } from '@my/core';
import { NotificationType } from '.';

import { NotificationService } from './notification.service';
import { environment } from 'src/environments/environment';

describe('NotificationService', () => {
  const message = 'Notificación al usuario'
  const message2 = 'Notificación al usuario (2)'
  let service: NotificationService;
  let log: LoggerService;

  describe('Aislada', () => {
    beforeEach(() => {
      log = new LoggerService(0);
      service = new NotificationService(log);
      spyOn(log, 'error').and.stub();
    });

    describe('OK', () => {
      it('add message: error', (done: DoneFn) => {
        service.Notificacion.subscribe(
          {
            next: data => { expect(data.Message).toBe(message); done(); },
            error: () => fail()
          }
        );
        service.add(message)
        expect(service.HayNotificaciones).toBeTruthy();
        expect(service.Listado.length).toBe(1);
        expect(service.Listado[0].Id).toBe(1);
        expect(service.Listado[0].Message).toBe(message);
        expect(service.Listado[0].Type).toBe(NotificationType.error);
        if (!environment.production) {
          expect(log.error).toHaveBeenCalled();
          expect(log.error).toHaveBeenCalledWith(`NOTIFICATION: ${message}`)
        }
      });

      it('add message: warn', (done: DoneFn) => {
        service.Notificacion.subscribe({
          next: data => { expect(data.Message).toBe(message); done(); },
          error: () => fail()
        });
        service.add(message, NotificationType.warn)
        expect(service.HayNotificaciones).toBeTruthy();
        expect(service.Listado.length).toBe(1);
        expect(service.Listado[0].Id).toBe(1);
        expect(service.Listado[0].Message).toBe(message);
        expect(service.Listado[0].Type).toBe(NotificationType.warn);
        expect(log.error).not.toHaveBeenCalled();
      });

      it('remove message', () => {
        service.add(message)
        service.add(message2)
        expect(service.HayNotificaciones).toBeTruthy();
        expect(service.Listado.length).toBe(2);
        service.remove(0)
        expect(service.Listado.length).toBe(1);
        expect(service.Listado[0].Id).toBe(2);
        service.remove(0)
        expect(service.HayNotificaciones).toBeFalsy();
      });

      it('clear messages', () => {
        service.add(message)
        service.add(message2)
        expect(service.HayNotificaciones).toBeTruthy();
        expect(service.Listado.length).toBe(2);
        service.clear()
        expect(service.HayNotificaciones).toBeFalsy();
      });

    })
    describe('KO', () => {
      it('add message: sin mensaje', () => {
        service.add('')
        expect(log.error).toHaveBeenCalled();
        expect(log.error).toHaveBeenCalledWith('Falta el mensaje de notificación.')
      });
      it('remove: fuera de rango', () => {
        service.remove(1)
        expect(log.error).toHaveBeenCalled();
        expect(log.error).toHaveBeenCalledWith('Index out of range.')
      });

    })
  })

  describe('Integración', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        providers: [LoggerService],
      });
      service = TestBed.inject(NotificationService);
      log = TestBed.inject(LoggerService);
      spyOn(log, 'error');
    });

    it('should be created', () => {
      expect(service).toBeTruthy();
    });
  })
});
