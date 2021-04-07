import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { FragranceComponent } from './list/fragrance.component';
import { FragranceDetailComponent } from './detail/fragrance-detail.component';
import { FragranceUpdateComponent } from './update/fragrance-update.component';
import { FragranceDeleteDialogComponent } from './delete/fragrance-delete-dialog.component';
import { FragranceRoutingModule } from './route/fragrance-routing.module';

@NgModule({
  imports: [SharedModule, FragranceRoutingModule],
  declarations: [FragranceComponent, FragranceDetailComponent, FragranceUpdateComponent, FragranceDeleteDialogComponent],
  entryComponents: [FragranceDeleteDialogComponent],
})
export class FragranceModule {}
