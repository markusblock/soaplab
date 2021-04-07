import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFragrance } from '../fragrance.model';
import { FragranceService } from '../service/fragrance.service';

@Component({
  templateUrl: './fragrance-delete-dialog.component.html',
})
export class FragranceDeleteDialogComponent {
  fragrance?: IFragrance;

  constructor(protected fragranceService: FragranceService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fragranceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
