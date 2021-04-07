import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFat } from '../fat.model';

@Component({
  selector: 'jhi-fat-detail',
  templateUrl: './fat-detail.component.html',
})
export class FatDetailComponent implements OnInit {
  fat: IFat | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fat }) => {
      this.fat = fat;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
