import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFragrance } from '../fragrance.model';

@Component({
  selector: 'jhi-fragrance-detail',
  templateUrl: './fragrance-detail.component.html',
})
export class FragranceDetailComponent implements OnInit {
  fragrance: IFragrance | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fragrance }) => {
      this.fragrance = fragrance;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
