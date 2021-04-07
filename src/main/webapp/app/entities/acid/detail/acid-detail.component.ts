import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAcid } from '../acid.model';

@Component({
  selector: 'jhi-acid-detail',
  templateUrl: './acid-detail.component.html',
})
export class AcidDetailComponent implements OnInit {
  acid: IAcid | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ acid }) => {
      this.acid = acid;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
