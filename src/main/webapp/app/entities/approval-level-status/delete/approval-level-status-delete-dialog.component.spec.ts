jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ApprovalLevelStatusService } from '../service/approval-level-status.service';

import { ApprovalLevelStatusDeleteDialogComponent } from './approval-level-status-delete-dialog.component';

describe('Component Tests', () => {
  describe('ApprovalLevelStatus Management Delete Component', () => {
    let comp: ApprovalLevelStatusDeleteDialogComponent;
    let fixture: ComponentFixture<ApprovalLevelStatusDeleteDialogComponent>;
    let service: ApprovalLevelStatusService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ApprovalLevelStatusDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(ApprovalLevelStatusDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ApprovalLevelStatusDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ApprovalLevelStatusService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
