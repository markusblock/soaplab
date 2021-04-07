import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFragrance } from '../fragrance.model';
import { FragranceService } from '../service/fragrance.service';
import { FragranceDeleteDialogComponent } from '../delete/fragrance-delete-dialog.component';

@Component({
  selector: 'jhi-fragrance',
  templateUrl: './fragrance.component.html',
})
export class FragranceComponent implements OnInit {
  fragrances?: IFragrance[];
  isLoading = false;

  constructor(protected fragranceService: FragranceService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.fragranceService.query().subscribe(
      (res: HttpResponse<IFragrance[]>) => {
        this.isLoading = false;
        this.fragrances = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IFragrance): number {
    return item.id!;
  }

  delete(fragrance: IFragrance): void {
    const modalRef = this.modalService.open(FragranceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.fragrance = fragrance;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
