import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LoggerService } from '@my/core';
import { AjaxWaitComponent, NotificationComponent, NotificationModalComponent } from './main';
import { NotificationService, NotificationType } from './common-services';
import { HeaderComponent } from "./main/header/header.component";
import { FooterComponent } from "./main/footer/footer.component";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NotificationComponent, /*NotificationModalComponent,*/
    AjaxWaitComponent, HeaderComponent, FooterComponent],
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
