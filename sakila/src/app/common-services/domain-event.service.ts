/* eslint-disable @typescript-eslint/no-explicit-any */
import { Injectable } from '@angular/core';
import { Subject, Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';

export class EventData {
  constructor(private name: string, private value?: any) {}
  public get Name() { return this.name }
  public get Value() { return this.value }
}

/**
 * El patrón Event Bus básicamente permite a objetos suscribirse a ciertos eventos del Bus,
 * de modo que cuando un evento es publicado en el Bus se propaga a cualquier suscriptor interesado.
 */
@Injectable({
  providedIn: 'root'
})
export class EventBusService {
  private subject$ = new Subject<EventData>();

  emit(event: EventData): void;
  emit(name: string, value?: any): void;
  emit(eventOrName: EventData | string, value?: any): void {
    if (eventOrName instanceof EventData) {
      this.subject$.next(eventOrName);
    } else {
      this.subject$.next(new EventData(eventOrName ?? '', value))
    }
  }

  on(eventName: string, action: any): Subscription {
    return this.subject$.pipe(
      filter((e: EventData) => e.Name === eventName),
      map((e: EventData) => e.Value)
    ).subscribe(action);
  }
}
