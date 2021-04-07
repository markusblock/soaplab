import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAcid, Acid } from '../acid.model';
import { AcidService } from '../service/acid.service';

@Component({
  selector: 'jhi-acid-update',
  templateUrl: './acid-update.component.html',
})
export class AcidUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    inci: [],
  });

  constructor(protected acidService: AcidService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ acid }) => {
      this.updateForm(acid);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const acid = this.createFromForm();
    if (acid.id !== undefined) {
      this.subscribeToSaveResponse(this.acidService.update(acid));
    } else {
      this.subscribeToSaveResponse(this.acidService.create(acid));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAcid>>): void {
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

  protected updateForm(acid: IAcid): void {
    this.editForm.patchValue({
      id: acid.id,
      name: acid.name,
      inci: acid.inci,
    });
  }

  protected createFromForm(): IAcid {
    return {
      ...new Acid(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      inci: this.editForm.get(['inci'])!.value,
    };
  }
}
