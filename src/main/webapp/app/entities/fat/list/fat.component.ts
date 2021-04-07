import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFat } from '../fat.model';
import { FatService } from '../service/fat.service';
import { FatDeleteDialogComponent } from '../delete/fat-delete-dialog.component';

@Component({
  selector: 'jhi-fat',
  templateUrl: './fat.component.html',
})
export class FatComponent implements OnInit {
  fats?: IFat[];
  isLoading = false;

  constructor(protected fatService: FatService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.fatService.query().subscribe(
      (res: HttpResponse<IFat[]>) => {
        this.isLoading = false;
        this.fats = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IFat): number {
    return item.id!;
  }

  delete(fat: IFat): void {
    const modalRef = this.modalService.open(FatDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.fat = fat;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
