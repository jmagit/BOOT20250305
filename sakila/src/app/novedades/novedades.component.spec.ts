import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NovedadesComponent } from './novedades.component';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

describe('NovedadesComponent', () => {
  let component: NovedadesComponent;
  let fixture: ComponentFixture<NovedadesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [NovedadesComponent],
      providers: [provideHttpClient(), provideHttpClientTesting(),],
    });
    fixture = TestBed.createComponent(NovedadesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
