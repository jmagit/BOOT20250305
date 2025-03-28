import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LoggerService } from '@my/core';
import { DemosComponent } from './ejemplos/demos/demos.component';
import { NotificationComponent, NotificationModalComponent } from './main';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, DemosComponent, NotificationComponent, NotificationModalComponent, ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title: string = 'world';

  // constructor(out: LoggerService) {
  //   out.error('Es un error')
  //   out.warn('Es un warn')
  //   out.info('Es un info')
  //   out.log('Es un log')
  // }
}
