import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { FatComponent } from './list/fat.component';
import { FatDetailComponent } from './detail/fat-detail.component';
import { FatUpdateComponent } from './update/fat-update.component';
import { FatDeleteDialogComponent } from './delete/fat-delete-dialog.component';
import { FatRoutingModule } from './route/fat-routing.module';

@NgModule({
  imports: [SharedModule, FatRoutingModule],
  declarations: [FatComponent, FatDetailComponent, FatUpdateComponent, FatDeleteDialogComponent],
  entryComponents: [FatDeleteDialogComponent],
})
export class FatModule {}
