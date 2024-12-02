import { Component, OnInit } from '@angular/core';
import { VersionService } from './services/version.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',  
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  constructor(private versionService: VersionService) { }
  title = 'cashcontroller-app';
  version: string = "";

  ngOnInit(): void {
    this.getVersion();  
  }
  getVersion() {
    this.versionService.getVersion().subscribe((res) => this.version = res);
  }
}
