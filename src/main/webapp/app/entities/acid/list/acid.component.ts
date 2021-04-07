import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAcid } from '../acid.model';
import { AcidService } from '../service/acid.service';
import { AcidDeleteDialogComponent } from '../delete/acid-delete-dialog.component';

@Component({
  selector: 'jhi-acid',
  templateUrl: './acid.component.html',
})
export class AcidComponent implements OnInit {
  acids?: IAcid[];
  isLoading = false;

  constructor(protected acidService: AcidService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.acidService.query().subscribe(
      (res: HttpResponse<IAcid[]>) => {
        this.isLoading = false;
        this.acids = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAcid): number {
    return item.id!;
  }

  delete(acid: IAcid): void {
    const modalRef = this.modalService.open(AcidDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.acid = acid;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
