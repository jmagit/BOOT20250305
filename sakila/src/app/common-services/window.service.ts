import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class WindowService {

  alert(msg: string) {
    window.alert(msg);
  }

  confirm(msg: string) {
    return window.confirm(msg);
  }

  reload() {
    window.location.reload();
  }

}
