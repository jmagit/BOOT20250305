import { Router, NavigationEnd, ActivationStart, GuardsCheckEnd } from '@angular/router';
import { LoggerService } from '@my/core';

import { Injectable } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { EventBusService } from './domain-event.service';

@Injectable({ providedIn: 'root' })
export class NavigationService {
  private readonly MAX_CACHE = 5
  private history: string[] = [];
  constructor(private router: Router, private title: Title, private logger: LoggerService, private eventBus: EventBusService) {
    router.events.subscribe(e => {
      if (e instanceof ActivationStart) {
        if (e.snapshot?.data?.['pageTitle']) {
          this.title.setTitle(e.snapshot.data['pageTitle']);
        } else {
          this.title.setTitle('Curso de Angular');
        }
      }
      if (e instanceof NavigationEnd && !e.url.includes('/login')) {
        this.history.push(e.url);
        if (this.history.length > this.MAX_CACHE) this.history.splice(0, 1)
        logger.log(`${this.history.length} NavigationEnd ${e.url}`);
      }
      if (e instanceof GuardsCheckEnd) {
        const ev = e as GuardsCheckEnd
        logger.log(`GuardsCheckEnd to ${e.url}: ${ev.shouldActivate}`);
      }
    });
  }

  back(defecto: string = '/', delta: number = 1) {
    while (delta && this.history.length > 0) {
      this.history.pop();
      delta--;
    }
    const url = this.history.pop() ?? defecto;
    this.router.navigateByUrl(url);
    this.logger.log(`Back to ${url}`);
  }
}
