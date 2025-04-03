import { TestBed } from '@angular/core/testing';
import { ERROR_LEVEL, LoggerService } from './logger.service';

describe('LoggerService', () => {
  describe('Sin ERROR_LEVEL', () => {
    let service: LoggerService;
    const message = 'Hola mundo';

    beforeEach(() => {
      TestBed.configureTestingModule({
        providers: [LoggerService]
      });
      service = TestBed.inject(LoggerService);
    });

    it('should be created', () => {
      expect(service).toBeTruthy();
    });

    it('console.error', () => {
      spyOn(console, 'error')
      service.error(message)
      expect(console.error).toHaveBeenCalled()
      expect(console.error).toHaveBeenCalledWith(message)
    });

    it('console.warn', () => {
      spyOn(console, 'warn')
      service.warn(message)
      expect(console.warn).toHaveBeenCalled()
      expect(console.warn).toHaveBeenCalledWith(message)
    });

    it('console.info', () => {
      spyOn(console, 'info')
      service.info(message)
      expect(console.info).toHaveBeenCalled()
      expect(console.info).toHaveBeenCalledWith(message)
    });

    xit('Sin console.info', () => {
      spyOn(console, 'info').and.returnValue(undefined)
      spyOn(console, 'log')
      service.info(message)
      expect(console.info).not.toHaveBeenCalled()
      expect(console.log).toHaveBeenCalled()
      expect(console.log).toHaveBeenCalledWith(message)
    });

    it('console.log', () => {
      spyOn(console, 'log')
      service.log(message)
      expect(console.log).toHaveBeenCalled()
      expect(console.log).toHaveBeenCalledWith(message)
    });

  });
  describe('Con ERROR_LEVEL = 0', () => {
    let service: LoggerService;
    const message = 'Hola mundo';

    beforeEach(() => {
      TestBed.configureTestingModule({
        providers: [LoggerService, { provide: ERROR_LEVEL, useValue: 0 }]
      });
      service = TestBed.inject(LoggerService);
    });

    it('should be created', () => {
      expect(service).toBeTruthy();
    });

    it('console.error', () => {
      spyOn(console, 'error')
      service.error(message)
      expect(console.error).not.toHaveBeenCalled()
    });

    it('console.warn', () => {
      spyOn(console, 'warn')
      service.warn(message)
      expect(console.warn).not.toHaveBeenCalled()
    });

    it('console.info', () => {
      spyOn(console, 'info')
      service.info(message)
      expect(console.info).not.toHaveBeenCalled()
    });

    it('console.log', () => {
      spyOn(console, 'log')
      service.log(message)
      expect(console.log).not.toHaveBeenCalled()
    });

  });
});
