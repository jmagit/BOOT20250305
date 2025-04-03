import { NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService, LoginComponent } from 'src/app/security';

@Component({
  selector: 'app-header',
  imports: [RouterLink, RouterLinkActive, LoginComponent],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  constructor(public auth: AuthService) {}
}
