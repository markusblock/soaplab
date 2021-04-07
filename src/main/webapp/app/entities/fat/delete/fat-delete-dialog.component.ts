import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFat } from '../fat.model';
import { FatService } from '../service/fat.service';

@Component({
  templateUrl: './fat-delete-dialog.component.html',
})
export class FatDeleteDialogComponent {
  fat?: IFat;

  constructor(protected fatService: FatService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fatService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
