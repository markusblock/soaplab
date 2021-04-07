import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAcid } from '../acid.model';
import { AcidService } from '../service/acid.service';

@Component({
  templateUrl: './acid-delete-dialog.component.html',
})
export class AcidDeleteDialogComponent {
  acid?: IAcid;

  constructor(protected acidService: AcidService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.acidService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
