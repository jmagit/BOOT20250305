import { ComponentFixture, TestBed } from '@angular/core/testing';
import GraficoSvgComponent from './grafico-svg.component';

describe('GraficoSvgComponent', () => {
  let component: GraficoSvgComponent;
  let fixture: ComponentFixture<GraficoSvgComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ GraficoSvgComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GraficoSvgComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it('changeColor', () => {
    spyOn(Math, 'random').and.returnValues(0.1, 0.2, 0.3)
    component.changeColor()
    expect(component.fillColor).toBe('rgb(25, 51, 76)');
  });
});
