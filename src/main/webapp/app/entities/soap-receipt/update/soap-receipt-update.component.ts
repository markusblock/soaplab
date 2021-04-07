import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISoapReceipt, SoapReceipt } from '../soap-receipt.model';
import { SoapReceiptService } from '../service/soap-receipt.service';
import { IFat } from 'app/entities/fat/fat.model';
import { FatService } from 'app/entities/fat/service/fat.service';

@Component({
  selector: 'jhi-soap-receipt-update',
  templateUrl: './soap-receipt-update.component.html',
})
export class SoapReceiptUpdateComponent implements OnInit {
  isSaving = false;

  fatsSharedCollection: IFat[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    liquid: [],
    superfat: [],
    fats: [],
  });

  constructor(
    protected soapReceiptService: SoapReceiptService,
    protected fatService: FatService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ soapReceipt }) => {
      this.updateForm(soapReceipt);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const soapReceipt = this.createFromForm();
    if (soapReceipt.id !== undefined) {
      this.subscribeToSaveResponse(this.soapReceiptService.update(soapReceipt));
    } else {
      this.subscribeToSaveResponse(this.soapReceiptService.create(soapReceipt));
    }
  }

  trackFatById(index: number, item: IFat): number {
    return item.id!;
  }

  getSelectedFat(option: IFat, selectedVals?: IFat[]): IFat {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISoapReceipt>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(soapReceipt: ISoapReceipt): void {
    this.editForm.patchValue({
      id: soapReceipt.id,
      name: soapReceipt.name,
      liquid: soapReceipt.liquid,
      superfat: soapReceipt.superfat,
      fats: soapReceipt.fats,
    });

    this.fatsSharedCollection = this.fatService.addFatToCollectionIfMissing(this.fatsSharedCollection, ...(soapReceipt.fats ?? []));
  }

  protected loadRelationshipsOptions(): void {
    this.fatService
      .query()
      .pipe(map((res: HttpResponse<IFat[]>) => res.body ?? []))
      .pipe(map((fats: IFat[]) => this.fatService.addFatToCollectionIfMissing(fats, ...(this.editForm.get('fats')!.value ?? []))))
      .subscribe((fats: IFat[]) => (this.fatsSharedCollection = fats));
  }

  protected createFromForm(): ISoapReceipt {
    return {
      ...new SoapReceipt(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      liquid: this.editForm.get(['liquid'])!.value,
      superfat: this.editForm.get(['superfat'])!.value,
      fats: this.editForm.get(['fats'])!.value,
    };
  }
}
