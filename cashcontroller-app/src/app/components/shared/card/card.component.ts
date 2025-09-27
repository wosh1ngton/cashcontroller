import { Component, input } from '@angular/core';
import { PrimengModule } from 'src/app/primeng/primeng.module';

@Component({
  selector: 'app-card',
  standalone: true,
  imports: [PrimengModule],
  templateUrl: './card.component.html',
  styleUrl: './card.component.css'
})
export class CardComponent {

  tituloCard = input.required<string>();

}
