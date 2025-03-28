import { TestBed } from '@angular/core/testing';
import { ERROR_LEVEL, LoggerService } from '@my/core';
import { NavigationService } from './navigation.service';

describe('NavigationService', () => {
  let service: NavigationService;
  let log: LoggerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ LoggerService, { provide: ERROR_LEVEL, useValue: 0 } ],
    });
    service = TestBed.inject(NavigationService);
    log = TestBed.inject(LoggerService);
    spyOn(log, 'log');
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('go back', () => {
    service.back();
    expect(log.log).toHaveBeenCalled();
  });

});
