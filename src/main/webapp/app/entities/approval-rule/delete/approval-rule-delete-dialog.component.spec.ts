jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ApprovalRuleService } from '../service/approval-rule.service';

import { ApprovalRuleDeleteDialogComponent } from './approval-rule-delete-dialog.component';

describe('Component Tests', () => {
  describe('ApprovalRule Management Delete Component', () => {
    let comp: ApprovalRuleDeleteDialogComponent;
    let fixture: ComponentFixture<ApprovalRuleDeleteDialogComponent>;
    let service: ApprovalRuleService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ApprovalRuleDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(ApprovalRuleDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ApprovalRuleDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ApprovalRuleService);
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
