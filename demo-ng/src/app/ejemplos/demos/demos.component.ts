import { Component } from '@angular/core';
import { Unsubscribable } from 'rxjs';
import { NotificationService, NotificationType } from 'src/app/common-services';

@Component({
  selector: 'app-demos',
  imports: [],
  templateUrl: './demos.component.html',
  styleUrl: './demos.component.css'
})
export class DemosComponent {
  constructor(public vm: NotificationService) { }

  // private suscriptor: Unsubscribable | undefined;
  private suscriptor?: Unsubscribable;

  ngOnInit(): void {
    this.suscriptor = this.vm.Notificacion.subscribe({
      next: n => {
        if (n.Type !== NotificationType.error) { return; }
        // window.alert(`Suscripción: ${n.Message}`);
        // this.vm.remove(this.vm.Listado.length - 1);
      },
      complete: () => this.suscriptor?.unsubscribe()
    });
  }

  ngOnDestroy(): void {
    if (this.suscriptor) {
      this.suscriptor.unsubscribe();
    }
  }

}
