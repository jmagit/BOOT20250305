import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LoggerService } from '@my/core';
import { DemosComponent } from './ejemplos/demos/demos.component';
import { NotificationComponent, NotificationModalComponent } from './main';
import { NotificationService, NotificationType } from './common-services';
import { FormulariosComponent } from './ejemplos/formularios/formularios.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, FormulariosComponent, NotificationComponent, /*NotificationModalComponent,*/ ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {

  // constructor(out: LoggerService) {
  //   out.error('Es un error')
  //   out.warn('Es un warn')
  //   out.info('Es un info')
  //   out.log('Es un log')
  // }
  // constructor(private notify: NotificationService) {}

  // ngOnInit(): void {
  //   this.notify.add('Aplicación arrancada', NotificationType.info)
  // }
}
