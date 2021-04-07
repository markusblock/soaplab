import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AcidComponent } from './list/acid.component';
import { AcidDetailComponent } from './detail/acid-detail.component';
import { AcidUpdateComponent } from './update/acid-update.component';
import { AcidDeleteDialogComponent } from './delete/acid-delete-dialog.component';
import { AcidRoutingModule } from './route/acid-routing.module';

@NgModule({
  imports: [SharedModule, AcidRoutingModule],
  declarations: [AcidComponent, AcidDetailComponent, AcidUpdateComponent, AcidDeleteDialogComponent],
  entryComponents: [AcidDeleteDialogComponent],
})
export class AcidModule {}
